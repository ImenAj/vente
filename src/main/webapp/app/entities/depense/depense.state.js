(function() {
    'use strict';

    angular
        .module('pfeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('depense', {
            parent: 'entity',
            url: '/depense',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pfeApp.depense.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/depense/depenses.html',
                    controller: 'DepenseController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('depense');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('depense-detail', {
            parent: 'depense',
            url: '/depense/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pfeApp.depense.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/depense/depense-detail.html',
                    controller: 'DepenseDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('depense');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Depense', function($stateParams, Depense) {
                    return Depense.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'depense',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('depense-detail.edit', {
            parent: 'depense-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/depense/depense-dialog.html',
                    controller: 'DepenseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Depense', function(Depense) {
                            return Depense.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('depense.new', {
            parent: 'depense',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/depense/depense-dialog.html',
                    controller: 'DepenseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                date_depense: null,
                                date_echeance: null,
                                reference: null,
                                observation: null,
                                document: null,
                                documentContentType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('depense', null, { reload: 'depense' });
                }, function() {
                    $state.go('depense');
                });
            }]
        })
        .state('depense.edit', {
            parent: 'depense',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/depense/depense-dialog.html',
                    controller: 'DepenseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Depense', function(Depense) {
                            return Depense.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('depense', null, { reload: 'depense' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('depense.delete', {
            parent: 'depense',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/depense/depense-delete-dialog.html',
                    controller: 'DepenseDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Depense', function(Depense) {
                            return Depense.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('depense', null, { reload: 'depense' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
