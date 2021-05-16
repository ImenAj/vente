(function() {
    'use strict';

    angular
        .module('pfeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('sous-famille', {
            parent: 'entity',
            url: '/sous-famille',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pfeApp.sous_famille.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sous-famille/sous-familles.html',
                    controller: 'Sous_familleController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('sous_famille');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('sous-famille-detail', {
            parent: 'sous-famille',
            url: '/sous-famille/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pfeApp.sous_famille.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sous-famille/sous-famille-detail.html',
                    controller: 'Sous_familleDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('sous_famille');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Sous_famille', function($stateParams, Sous_famille) {
                    return Sous_famille.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'sous-famille',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('sous-famille-detail.edit', {
            parent: 'sous-famille-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sous-famille/sous-famille-dialog.html',
                    controller: 'Sous_familleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Sous_famille', function(Sous_famille) {
                            return Sous_famille.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sous-famille.new', {
            parent: 'sous-famille',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sous-famille/sous-famille-dialog.html',
                    controller: 'Sous_familleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                libelleSF: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('sous-famille', null, { reload: 'sous-famille' });
                }, function() {
                    $state.go('sous-famille');
                });
            }]
        })
        .state('sous-famille.edit', {
            parent: 'sous-famille',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sous-famille/sous-famille-dialog.html',
                    controller: 'Sous_familleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Sous_famille', function(Sous_famille) {
                            return Sous_famille.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('sous-famille', null, { reload: 'sous-famille' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sous-famille.delete', {
            parent: 'sous-famille',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sous-famille/sous-famille-delete-dialog.html',
                    controller: 'Sous_familleDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Sous_famille', function(Sous_famille) {
                            return Sous_famille.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('sous-famille', null, { reload: 'sous-famille' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
