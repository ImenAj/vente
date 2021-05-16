(function() {
    'use strict';

    angular
        .module('pfeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('mode-de-paiment', {
            parent: 'entity',
            url: '/mode-de-paiment',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pfeApp.mode_de_paiment.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/mode-de-paiment/mode-de-paiments.html',
                    controller: 'Mode_de_paimentController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('mode_de_paiment');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('mode-de-paiment-detail', {
            parent: 'mode-de-paiment',
            url: '/mode-de-paiment/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pfeApp.mode_de_paiment.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/mode-de-paiment/mode-de-paiment-detail.html',
                    controller: 'Mode_de_paimentDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('mode_de_paiment');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Mode_de_paiment', function($stateParams, Mode_de_paiment) {
                    return Mode_de_paiment.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'mode-de-paiment',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('mode-de-paiment-detail.edit', {
            parent: 'mode-de-paiment-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mode-de-paiment/mode-de-paiment-dialog.html',
                    controller: 'Mode_de_paimentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Mode_de_paiment', function(Mode_de_paiment) {
                            return Mode_de_paiment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('mode-de-paiment.new', {
            parent: 'mode-de-paiment',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mode-de-paiment/mode-de-paiment-dialog.html',
                    controller: 'Mode_de_paimentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                mode: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('mode-de-paiment', null, { reload: 'mode-de-paiment' });
                }, function() {
                    $state.go('mode-de-paiment');
                });
            }]
        })
        .state('mode-de-paiment.edit', {
            parent: 'mode-de-paiment',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mode-de-paiment/mode-de-paiment-dialog.html',
                    controller: 'Mode_de_paimentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Mode_de_paiment', function(Mode_de_paiment) {
                            return Mode_de_paiment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('mode-de-paiment', null, { reload: 'mode-de-paiment' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('mode-de-paiment.delete', {
            parent: 'mode-de-paiment',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mode-de-paiment/mode-de-paiment-delete-dialog.html',
                    controller: 'Mode_de_paimentDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Mode_de_paiment', function(Mode_de_paiment) {
                            return Mode_de_paiment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('mode-de-paiment', null, { reload: 'mode-de-paiment' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
